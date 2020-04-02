const express = require("express");
const router = express.Router();

router.get("/getUser/:userID", (req,res) => {
    let database = req.app.get("database")
    var targetUser = database.ref('User/User' + req.param('userID'));
    targetUser.once("value", function(snapshot){
        res.end(JSON.stringify(snapshot.val()));
    })
})

router.delete("/deleteUser/:userID", (req, res) => {

    let database = req.app.get("database")
    let userToDel = database.ref('User/User' + req.param('userID'))
    userToDel.set({})

    res.end("deleted from the DB!")

})

router.post("/sendAcceptReqNotif/:notifID", (req,res)=>{
	const PushNotifications = require('@pusher/push-notifications-server');

	let beamsClient = new PushNotifications({
		instanceId: '211e38a9-4bc8-40c5-958a-4a7f9aa91547',
		secretKey: '899F51F33EF49FF687ABA0D6512A626B8A62AE96BBBAB4A217F4411925AAF348'
	});
	
	var interest = req.param('notifID')

	beamsClient.publishToInterests([interest], {
		fcm: {
		notification: {
		title: 'Notification',
		body: 'Your request has been accepted! Start chatting now!'
		}
	}
	}).then((publishResponse) => {
		console.log('Just published:', publishResponse.publishId);
		return null;
	}).catch((error) => {
		console.log('Error:', error);
	});
	res.end("Posted!");
})

router.post("/sendRejectReqNotif/:notifID", (req,res)=>{
	const PushNotifications = require('@pusher/push-notifications-server');

	let beamsClient = new PushNotifications({
		instanceId: '211e38a9-4bc8-40c5-958a-4a7f9aa91547',
		secretKey: '899F51F33EF49FF687ABA0D6512A626B8A62AE96BBBAB4A217F4411925AAF348'
	});

	var interest = req.param('notifID')

	beamsClient.publishToInterests([interest], {
		fcm: {
		notification: {
		title: 'Notification',
		body: 'Your request has been rejected!'
		}
	}
	}).then((publishResponse) => {
		console.log('Just published:', publishResponse.publishId);
		return null;
	}).catch((error) => {
		console.log('Error:', error);
	});
	res.end("Posted!");
})

router.post("/sendNotifToHost/:notifID", (req,res)=>{
	const PushNotifications = require('@pusher/push-notifications-server');

	let beamsClient = new PushNotifications({
		instanceId: '211e38a9-4bc8-40c5-958a-4a7f9aa91547',
		secretKey: '899F51F33EF49FF687ABA0D6512A626B8A62AE96BBBAB4A217F4411925AAF348'
	});
	
	var interest = req.param('notifID')

	beamsClient.publishToInterests([interest], {
		fcm: {
		notification: {
		title: 'Notification',
		body: 'You received a request!'
		}
	}
	}).then((publishResponse) => {
		console.log('Just published:', publishResponse.publishId);
		return null;
	}).catch((error) => {
		console.log('Error:', error);
	});
	res.end("Posted!");
})

router.post("/sendChatNotif/:notifID", (req,res)=>{
	const PushNotifications = require('@pusher/push-notifications-server');

	let beamsClient = new PushNotifications({
		instanceId: '211e38a9-4bc8-40c5-958a-4a7f9aa91547',
		secretKey: '899F51F33EF49FF687ABA0D6512A626B8A62AE96BBBAB4A217F4411925AAF348'
	});
	
	var interest = req.param('notifID')

	beamsClient.publishToInterests([interest], {
		fcm: {
		notification: {
		title: 'Notification',
		body: 'You received a message!'
		}
	}
	}).then((publishResponse) => {
		console.log('Just published:', publishResponse.publishId);
		return null;
	}).catch((error) => {
		console.log('Error:', error);
	});
	res.end("Posted!");
})

router.get("/getUser/:userID", (req,res) => {
    let database = req.app.get("database")
    var targetUser = database.ref('User/User' + req.param('userID'));
    targetUser.once("value", function(snapshot){
        res.end(JSON.stringify(snapshot.val()));
    })
})

router.delete("/deleteUser/:userID", (req, res) => {

    let database = req.app.get("database")
    let userToDel = database.ref('User/User-' + req.param('userID'))
    userToDel.set({})

    res.end("deleted from the DB")

})

router.put("/updateUser/:userID", (req, res) => {

    let database = req.app.get("database")
    let userToUpdate = database.ref("User/User" + req.params.userID)

	userToUpdate.once("value", function(snapshot){
            newData = snapshot.val()
			userToUpdate.update(req.body)
        })
	
	res.json(req.body)
})

router.post("/addUser/:newID", (req, res) => { 
    let database = req.app.get("database")
    let userRef = database.ref("User")

	const newReq = userRef.child("User" + req.params.newID)
	req.body.userID = req.params.newID; 
	newReq.set(req.body);
	res.json(req.body);
    })

router.put("/updateInvitation/:invitationID", (req, res) => {

    let database = req.app.get("database")
    var invitationToUpdate = database.ref('Invitations/Invitation' + req.param('invitationID'));

	invitationToUpdate.once("value", function(snapshot){
            newData = snapshot.val()
			invitationToUpdate.update(req.body)
        })
	res.json(req.body)
})

module.exports = router;
