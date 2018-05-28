const functions = require('firebase-functions');

const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendNotification = functions.firestore.document("{category}/{name}").onWrite(event => {
	console.log('Push notification event triggered');
	const category = event.params.category;
	const name     = event.params.name;
	return admin.firestore().collection(category).doc(name).get().then(queryResult =>{
	const summary = queryResult.data().summary;
	const payload = {
        notification: {
            title: category,
            body: summary,
            sound: "default"
        },
        data: {
            title: category,
            message: summary
        }
    };
/* Create an options object that contains the time to live for the notification and the priority. */
    const options = {
        priority: "high",
        timeToLive: 60 * 60 * 24 //24 hours
    };	
return admin.messaging().sendToTopic("notifications", payload, options);
});
});
