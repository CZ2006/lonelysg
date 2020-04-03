const express = require("express");
const router = express.Router();


router.get("/getMessages/:user1/:user2", (req,res)=>{ 
    //Call using http://localhost:5001/lonely-4a186/us-central1/app/MinHui/getMessages after running firebase serve
    let database = req.app.get("database")
    var ref = database.ref('Messages');
    var user1 = req.params.user1;
    var user2 = req.params.user2;

    function compare(a, b) {
        if (a.MessageID > b.MessageID) return 1;
        if (b.MessageID > a.MessageID) return -1;
      
        return 0;
      }
    
    ref.once("value", function(snapshot){
        var messages = snapshot.val();
        var messagesArray = [];
        for (var i in messages){
            if ((messages[i].Receiver==(user2))&(messages[i].Sender==(user1))){
                messagesArray.push(messages[i]);
            }
            else if ((messages[i].Sender==(user2))&(messages[i].Receiver==(user1))){
                messagesArray.push(messages[i]);
            }
        }
        messagesArray.sort(compare);
        res.json(messagesArray); 
    })
})

router.get("/getChatUsersList/:userID", (req,res)=>{ 
    //Call using http://localhost:5001/lonely-4a186/us-central1/app/MinHui/getChatUsersList after running firebase serve
    
    let database = req.app.get("database")
    var ref = database.ref('Messages');
    var user = req.params.userID;
    
    ref.once("value", function(snapshot){
        var messages = snapshot.val();
        var chatUsersArray = [];
        for (var i in messages){
            var exists = 0;
            if (messages[i].Sender == user){
                for(var j in chatUsersArray){
                    exists = 0;
                    if (messages[i].Receiver==(chatUsersArray[j])){
                        exists = 1;
                        break;
                    }
                }
                if (exists==0){
                    chatUsersArray.push(messages[i].Receiver);
                }
            }
            if (messages[i].Receiver==user){
                exists = 0;
                for(var k in chatUsersArray){
                    if (messages[i].Sender==(chatUsersArray[k])){
                        exists = 1;
                        break;
                    }
                }
                if (exists==0){
                    chatUsersArray.push(messages[i].Sender);
                }
            }
        }
        res.json(chatUsersArray); //Returned in the browser or postman
    })

    

})

router.post("/sendMessage", (req, res) => { 
    //Call using http://localhost:5001/lonely-4a186/us-central1/app/MinHui/sendMessage 
    let database = req.app.get("database")
    let msgRef = database.ref("Messages")
    
    msgRef.once("value", function(snapshot){
        var messages = snapshot.val();
        var newID = 0;
        for (var i in messages){
            newID++;
        }
        const newMessage = msgRef.child("Message" + newID) 
        req.body.MessageID = newID; 
        newMessage.set(req.body)
        res.json(req.body); 
    })

})

router.get("/getInvitation/:invitationID", (req,res) => {
    let database = req.app.get("database")
    var ref = database.ref('Invitations/Invitation' + req.param('invitationID'));
    ref.once("value", function(snapshot){
        res.end(JSON.stringify(snapshot.val()));
    })
})

router.get("/getInvitations/:category/:user", (req,res)=>{ 
    //Call using http://localhost:5001/lonely-4a186/us-central1/app/MinHui/getInvitations after running firebase serve
    let database = req.app.get("database")
    var ref = database.ref('Invitations');
    var category = req.params.category;
    var user = req.params.user;

    function compare(a, b) {
        if (a.InvitationID > b.InvitationID) return 1;
        if (b.InvitationID > a.InvitationID) return -1;
      
        return 0;
      }
    
    ref.once("value", function(snapshot){
        var invitations = snapshot.val();
        var invitationsArray = [];
        if (category=="All"){
            for (var i in invitations){
                if ((invitations[i].Host!=user)&invitations[i].Category!="test"){
                    invitationsArray.push(invitations[i]);
                }
            }
        }
        else{
            for (var i in invitations){
                if ((invitations[i].Category==(category))&(invitations[i].Host!=user)){
                    invitationsArray.push(invitations[i]);
                }
            }
        }
        invitationsArray.sort(compare);
        res.json(invitationsArray); //Returned in the browser or postman
    })
})

router.get("/getUserInvitations/:user", (req,res)=>{ 
    //Call using http://localhost:5001/lonely-4a186/us-central1/app/MinHui/getUserInvitations after running firebase serve
    let database = req.app.get("database")
    var ref = database.ref('Invitations');
    var user = req.params.user;

    function compare(a, b) {
        if (a.InvitationID > b.InvitationID) return 1;
        if (b.InvitationID > a.InvitationID) return -1;
      
        return 0;
      }
    
    ref.once("value", function(snapshot){
        var invitations = snapshot.val();
        var invitationsArray = [];
        for (var i in invitations){
            if ((invitations[i].Host==(user))){
                invitationsArray.push(invitations[i]);
            }
        }
        invitationsArray.sort(compare);
        res.json(invitationsArray); //Returned in the browser or postman
    })
})

router.post("/addInvitation", (req, res) => { 
    //Call using http://localhost:5001/lonely-4a186/us-central1/app/MinHui/addInvitation in postman with sample data in the body after running firebase serve
    let database = req.app.get("database")
    let ref = database.ref("Invitations")

    ref.once("value", function(snapshot){
        var invitations = snapshot.val();
        var newID = 0;
        var invitationsArray = [];
        for (var i in invitations){
            invitationsArray.push(invitations[i]);
        }
        newID = invitationsArray[invitationsArray.length-1].InvitationID + 1;
        const newInvitation = ref.child("Invitation" + newID) //Requires information sent in JSON format
        req.body.InvitationID = newID; 
        newInvitation.set(req.body)

        res.end("Invitation added."); //Returned in postman
    })

})

router.delete("/deleteInvitation/:InvitationID", (req, res) => {

    let database = req.app.get("database")
    let invToDel = database.ref('Invitations/Invitation' + req.param('InvitationID'))
    invToDel.set({})

    res.end("Invitation deleted.")

})

router.get("/getReceivedRequests/:user", (req,res)=>{ 
    //Call using http://localhost:5001/lonely-4a186/us-central1/app/MinHui/getReceivedRequests after running firebase serve
    let database = req.app.get("database")
    var ref = database.ref('Requests');
    var user = req.params.user;
    
    function compare(a, b) {
        if (a.RequestID > b.RequestID) return 1;
        if (b.RequestID > a.RequestID) return -1;
      
        return 0;
      }
    
    ref.once("value", function(snapshot){
        var requests = snapshot.val();  
        var requestsArray = [];
        for (var i in requests){
			if (requests[i].Host==user){
				requestsArray.push(requests[i]);
			}
        }
        requestsArray.sort(compare);
        res.json(requestsArray); //Returned in the browser or postman
    })
})

router.get("/getPendingRequests/:user", (req,res)=>{ 
    //Call using http://localhost:5001/lonely-4a186/us-central1/app/MinHui/getPendingRequests after running firebase serve
    let database = req.app.get("database")
    var ref = database.ref('Requests');
    var user = req.params.user;
    
    function compare(a, b) {
        if (a.RequestID > b.RequestID) return 1;
        if (b.RequestID > a.RequestID) return -1;
      
        return 0;
      }
    
    ref.once("value", function(snapshot){
        var requests = snapshot.val();  
        var requestsArray = [];
        for (var i in requests){
			if (requests[i].Participant==user){
				requestsArray.push(requests[i]);
			}
        }
        requestsArray.sort(compare);
        res.json(requestsArray); //Returned in the browser or postman
    })
})

router.post("/sendRequest", (req, res) => {
    //Call using http://localhost:5001/lonely-4a186/us-central1/app/MinHui/sendRequest
    let database = req.app.get("database")
    let ref = database.ref("Requests")

    ref.once("value", function(snapshot){
        var requests = snapshot.val(); 
        var newID = 0;
        var requestsArray = [];
        for (var i in requests){
            requestsArray.push(requests[i]);
        }
        newID = requestsArray[requestsArray.length-1].RequestID + 1;
        const newReq = ref.child("Request" + newID) //Requires information sent in JSON format
        req.body.RequestID = newID; 
        newReq.set(req.body);
		res.json(req.body)
    })
    
})

router.delete("/deleteRequest/:RequestID", (req, res) => {

    let database = req.app.get("database")
    let reqToDel = database.ref('Requests/Request' + req.param('RequestID'))
    reqToDel.set({})

    res.end("Request deleted.")

})

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

module.exports = router;