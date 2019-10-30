# Instagram-Clone
This is a clone of Instagram with basic features.
## Screenshots
![alt text](https://github.com/rheo-chiti/InstagramClone/blob/master/app/src/main/res/drawable/755370b5-d145-4b6b-9c15-25f9819f2b6c.jfif) ![alt text](https://github.com/rheo-chiti/InstagramClone/blob/master/app/src/main/res/drawable/4c82aef8-4c23-42b6-8be9-b9e9a59f085a%20(1).jfif) ![alt text](https://github.com/rheo-chiti/InstagramClone/blob/master/app/src/main/res/drawable/032e3638-100e-4888-94bd-13c88c84af45.jfif)

## Features

- Sign-in/Sign-up

- Upload image

- View all posts

- Like/Comment on post

## Technologies used

- [Firebase Password-Based Authentication](https://firebase.google.com/docs/auth/android/password-auth)

- [Firebase Cloud Firestore](https://firebase.google.com/docs/firestore/)

- [Firebase Cloud Storage](https://firebase.google.com/docs/storage/)

## Activities

- [Login/SignUp Activity](https://github.com/rheo-chiti/InstagramClone/blob/master/app/src/main/java/com/example/chat/MainActivity.java)

- [View all posts](https://github.com/rheo-chiti/InstagramClone/blob/master/app/src/main/java/com/example/chat/ViewPhoto.java)

- [Add post](https://github.com/rheo-chiti/InstagramClone/blob/master/app/src/main/java/com/example/chat/AddPhoto.java)

- [View all comments](https://github.com/rheo-chiti/InstagramClone/blob/master/app/src/main/java/com/example/chat/ViewComments.java)

## How to run this on your system

- Install Android Studio from [here](https://developer.android.com/studio)

- Install Git from [here](https://git-scm.com/downloads)

- Fork the repository 

- In Android Studio, File->New->Project from Version Control->Github

## How this app works

User creates his account via Firebase Password Based Authentication.When a new user is registered the ***userName*** is first checked from existing userNames and then user creates account.User views all the posts on the next activity.The posts are retrieved from Firebase Cloud Firestore, the structure is like this:-

| Collection/Document  | Fields |
| ------------- | ------------- |
| Photo  |   |
| TimeStamp  | userName , timeStamp , imageURL , likes  |       
| Likes  |   |       
| TimeStamp  | userName |       

When user creates a post,the image is uploaded at Firebase Cloud Storage ,then it's download URL is saved at ***imageURL***.When user likes a photo the ***likes*** value is incremented and username is added in the ***userName*** field,if user removes his like then it is removed and subsequently ***likes*** value is decremented.The comments are also retrieved from Firebase Cloud Firestore, the structure is like this:-

| Collection/Document  | Fields |
| ------------- | ------------- |
| Photo  |   |
| TimeStamp  | userName , timeStamp , imageURL , likes  |       
| Comments  |   |       
| TimeStamp  | userName , timeStamp , commentText |    

## Future Features

- Image Caching

- Push Notifications

- Chat Feature

## Devices Tested On

| Device  | Android Version | Skin/ROM |
| ------------- | ------------- | ------------- |
| Realme 1  |  Android 9.0 | ColorOS |
| OnePlus 5T  |  Android 9.0 | OxygenOS |
| Redmi Note 5 Pro  |  Android 9.0 | MIUI 10 |
