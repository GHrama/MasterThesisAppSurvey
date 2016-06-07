function onRequest(request, response, modules) {
	var logger = modules.logger;
	var body = request.body;
	var give_level = 100 - body.summarization_level;
	var collectionAccess = modules.collectionAccess;
	collectionAccess.collection('DummySensor')
                    .find({user_id : body.user_id},function (err, docs)
    {
        if (err) {
            logger.error('Query failed: '+ err);
            response.body.debug = err;
            logger.info(err);
            response.complete(500);
        } else {
          modules.utils.tempObjectStore.set('storeinfo', docs);
          if(give_level === 0){
              response.body = null;
              response.complete(200);
            }
            if(give_level === 100){
              response.body = docs;
              response.complete(200);
            }
            var count = docs.length;
            var docs_temp = [];
            logger.info("Number of readings for this user = "+count);
            // Count the number of entries for this user for this sensor
            // Calculate the number of readings to give for every couple of readings
            var temp = (count * give_level) / 100;
            logger.info("temp = "+temp);
            if(give_level === 75){
              temp_skip =  Math.floor( count / temp );
            }
            else{
              temp_skip =  Math.ceil( count / temp ); // round to upper integer
            }
            logger.info("temp_skip = "+temp_skip);
            //docs.splice(0,(temp - 1));
            
            var loop_number = Math.floor(count / temp_skip );
            var index = 0;
            
            while(index < docs.length){
              docs_temp.push(docs[index]);
              index += (temp_skip);
            }
            
            response.body = docs_temp;
            logger.info("Length of summarized data = "+docs_temp.length);
            response.complete(200);
        }
    });
	
	
}