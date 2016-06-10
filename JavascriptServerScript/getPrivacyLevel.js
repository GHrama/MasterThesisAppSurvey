function onRequest(request, response, modules) {
	var logger = modules.logger;
	var collectionAccess = modules.collectionAccess
	var query = request.params.query
	var body = request.body
	collectionAccess.collection('UserResponse')
                    .find({user_id : body.user_id , sensors : body.sensors, data_collectors: body.data_collectors, contexts: body.contexts}, function (err, docs)
    {
        if (err) {
            logger.error('Query failed: '+ err);
            response.body.debug = err;
            logger.info(err);
            response.complete(500);
        } else {
            modules.utils.tempObjectStore.set('storeinfo', docs);
            logger.info("length of match results = "+docs.length);
            // if user has not answered this, set to max privacy
            if(docs.length == 0){
              response.body.summarization_level = 100;
              response.complete(200);
            }
            var i;
            var save_i = 0;
            for(i = 1 ; i < docs.length ; i++){
              if(docs[i]._kmd.lmt > docs[save_i]._kmd.lmt){
                logger.info("Greater timestamp = "+docs[i].timestamp+" i = "+i);
                save_i = i;
              }
            }
            var privacy_level = docs[save_i].privacy_level
            logger.info("privacy_level = "+privacy_level);
            switch(privacy_level){
              case 1 : response.body.summarization_level = 0;break;
              case 2 : response.body.summarization_level = 25;break;
              case 3 : response.body.summarization_level = 50;break;
              case 4 : response.body.summarization_level = 75;break;
              case 5 : response.body.summarization_level = 100;break;
              default : response.body.summarization_level = 100;break;
            }
            logger.info("summarization = "+response.body.summarization_level);
            response.complete(200);
        }
    });
    
	
}

//getPrivacy_Level Hook custom endpoint