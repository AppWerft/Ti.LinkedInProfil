#Ti.LinkedInProfile

This is the titanium version of LinkedIn SDK for opening of profiles. thanks to Adam T. Armstrong from [sitegrafx.com](sitegrafx.com) for sponsoring. 

![](https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQPOr7ie7h6WqYwHZmsAOhwDY_TrfZ19aR9E_FEv10D-GBCIz24cA)
##Usage   

```javascript
var LI = require("de.appwerft.linkedinprofile");
  
LI.addEventListener("onsuccess",function(){});
LI.addEventListener("onerror",function(){});

LI.openOtherProfile({
	id : "adamtarmstrong",
	onsuccess : function() {},
	onerror : function(e) {
		console.log(e.error);
	},
});
LI.openCurrentProfile();
```
For result you can use callbacks or events.