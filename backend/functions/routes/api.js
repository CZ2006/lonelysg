const express = require("express");
const router = express.Router();

router.get("/getStuff", (req,res)=>{
    let database = req.app.get("database")
    var ref = database.ref('User');
    ref.once("value", function(snapshot){
        console.log(snapshot.val());
        res.end("OK!")
    })
})

module.exports = router;