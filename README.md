#Ti.LinkedInProfile

This is the titanium version of LinkedIn SDK for opening of profiles. thanks to Adam T. Armstrong from [sitegrafx.com](http://sitegrafx.com) for sponsoring. 

![](https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQPOr7ie7h6WqYwHZmsAOhwDY_TrfZ19aR9E_FEv10D-GBCIz24cA)
##Usage   

```javascript
var LI = require("de.appwerft.linkedinprofile");


  
LI.addEventListener("onsuccess",function(){});
LI.addEventListener("onerror",function(){});

LI.openOtherProfile({
	alert : ["LI not installed","LinkedInis not installed. Please go to playstore","Download","Cancel"], // texts for alert dialog
	memberId : "adamtarmstrong",
	onsuccess : function() {},
	onerror : function(e) {
		console.log(e.error);
	},
});
LI.openCurrentProfile({
	alert : ["LI not installed","LinkedInis not installed. Please go to playstore","Download","Cancel"], // texts for alert dialog
});
```
For result you can use callbacks or events.

You must identify your specific Android app with LinkedIn by adding some information about it to your LinkedIn application's settings.

```java
keytool -exportcert -keystore YOUR_RELEASE_KEY_PATH -alias YOUR_RELEASE_KEY_ALIAS | openssl sha1 -binary | openssl base64
```
![](https://content.linkedin.com/content/dam/developer/global/en_US/site/img/package_hash_values.png)