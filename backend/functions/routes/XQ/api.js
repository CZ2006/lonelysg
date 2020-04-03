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

router.post("/sendNotif", (req,res)=>{
	const PushNotifications = require('@pusher/push-notifications-server');

	let beamsClient = new PushNotifications({
		instanceId: '211e38a9-4bc8-40c5-958a-4a7f9aa91547',
		secretKey: '899F51F33EF49FF687ABA0D6512A626B8A62AE96BBBAB4A217F4411925AAF348'
	});

	beamsClient.publishToInterests(['debug-apple'], {
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

router.get("/getStuff", (req,res)=>{ //Sample function fetching data from the realtime DB on firebase
    //Call using http://localhost:5001/lonely-4a186/us-central1/app/XQ/getStuff after running firebase serve
    let database = req.app.get("database")
    var ref = database.ref('User');
    ref.once("value", function(snapshot){
        console.log(snapshot.val()); //View response value in the command line
        res.end("OK XQ!") //Returned in the browser or postman
    })
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

    res.end("deleted from the DB!")

})

router.put("/updateUser/:userID", (req, res) => {

    let database = req.app.get("database")
    let userToUpdate = database.ref("User/User" + req.params.userID)

	userToUpdate.once("value", function(snapshot){
            newData = snapshot.val()
			userToUpdate.update(req.body)
        })
	
	res.json(req.body)

    /*{
        "userID": 2,
        "password": "newPa55w0rd...",
        "interests": "Youtube, Marvel"
    } */

})

router.post("/addUser/:newID", (req, res) => { //Sample function adding new user to realtime DB on firebase
    //Call using http://localhost:5001/lonely-4a186/us-central1/app/XQ/addUser in postman with sample data in the body after running firebase serve
    let database = req.app.get("database")
    let userRef = database.ref("User")


	const newReq = userRef.child("User" + req.params.newID) //Requires information sent in JSON format
	req.body.userID = req.params.newID; 
	newReq.set(req.body);
	res.end("User added."); //Returned in postman
    })
 

    /* Sample data to test this function:

    {
        "userID": 2,
        "username": "RayRay",
        "password": "00ThisIs@GreatPassword!",
        "name": "Raymond",
        "email": "ray@email.com",
        "gender": "M",
        "birthday": "Undefined",
        "profilePic": "fantasticURL",
        "job": "NTU Student",
        "interests": "No idea"
    } */
//Update user
//Delete user

router.put("/updateInvitation/:invitationID", (req, res) => {

    let database = req.app.get("database")
    var invitationToUpdate = database.ref('Invitations/Invitation' + req.param('invitationID'));

	userToUpdate.once("value", function(snapshot){
            newData = snapshot.val()
			invitationToUpdate.update(req.body)
        })
	
	res.end("Invitation" + invitationToUpdate.invitationID + " updated!")

})

module.exports = router;
