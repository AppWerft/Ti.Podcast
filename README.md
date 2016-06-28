Ti.Podcast
==========

This is a simple podcast downloader. Input url, output json-object.

Usage
-----

```java
require("de.appwerft.podcast").loadPodcast({
    url : "",
    timeout : 10000,
    userAgent : "My smart downloader"
},function(_e) {
    console.log(_e.channel.title);
    console.log(_e.items[0].description);
});
```

