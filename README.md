#Ti.LinkedInProfile

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