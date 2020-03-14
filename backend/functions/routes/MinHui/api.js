const express = require("express");
const router = express.Router();

router.get("/getStuff", (req,res)=>{ //Sample function fetching data from the realtime DB on firebase
    //Call using http://localhost:5001/lonely-4a186/us-central1/app/MinHui/getStuff after running firebase serve
    let database = req.app.get("database")
    var ref = database.ref('User');
    ref.once("value", function(snapshot){
        console.log(snapshot.val());
        res.end("OK Min Hui!")
    })
})

module.exports = router;