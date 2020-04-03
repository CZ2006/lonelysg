const express = require("express");
const router = express.Router();

router.get("/getMessages/:user1/:user2", (req,res)=>{ 
   
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
        res.json(chatUsersArray); 
    })

    

})

router.post("/sendMessage", (req, res) => { 
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

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

module.exports = router;