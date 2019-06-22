# CollectorJS
Android App for CollectorJS

## Idea:
For CS602 (server-side web development), I had developed an express-js based website(http://collectorjs.herokuapp.com) to store the links for the articles that students were sharing on our discussion board. For current semester project, the idea is to extend this website and create a mobile app for the same. 
## Scope:
This app will be somewhere between Twitter (allowing limited character length of post) & reddit (allowing discussion of the given links and moderating links). The target audience is any tech enthusiasts who enjoy to share and discuss technology related articles.

## Highlight:
1.	Splash screen with vector drawable animation.
2.	Login screen
    -	Email Login
    -	Social Login
3.	Registration screen 
4.	Home screen listing all the post using recycler View
    -	Used constraint layout to arrange Image buttons inside Card View
    -	real time data update by using Realtime firebase database
    -	implemented readable and relatable posting day format, for e.g. 10 min ago, 1 day ago
    -	Upvote and Downvote counts
    -	comments count
5.	Comment Screen
     -	User can post new comments
     -	User can see comments posted by others
6.	Create Post screen
      -	real time integration with firebase, the post will appear on all the device immediately after posting
7.	Profile screen
      -	user can update their display image. Code has been added to crop and compress the image so that storing them on external server will not overload storage.
      -	user can update their display name.
8.	WebView:
      -	user can click on the post to open the link in webview and browse through the page while still remaining in the app. 

