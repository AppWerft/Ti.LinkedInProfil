#Ti.LinkedInProfile

##Usage   

```javascript
var LI = require("de.appwerft.linkedinprofile");

LI.addEventListener("onsuccess",function(){});
LI.addEventListener("onerror",function(){});

LI.openProfile({
	id : "adamtarmstrong",
	onsuccess : function() {},
	onerror : function(e) {
		console.log(e.error);
	},
});
```
For result you can use callbacks or events.