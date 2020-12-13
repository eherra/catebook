## Catebook

A social media platform for cats.

### about Catebook

The project has been made with a help of Java Spring boot, Spring MVC, Thymeleaf template engine for HTML modifications and some Bootstrap here and there. 

Main focus has been on website's back end programming, but front end was not left without attention. (The UX might not be the best)

### How to get started.

1. Clone the repository 
#### Choose directory and run from terminal:
```console
git clone https://github.com/eherra/catebook.git
```
2. Start the program
#### Terminal command to start the program from root folder (catebook):
```console
mvn compile exec:java -Dexec.mainClass=catebook.Main
```
3. From browser navigate to **localhost:/8080/register**. (in a case you opened the program on port 8080)

**Page like this appears:**
<img src="https://github.com/eherra/catebook/blob/main/src/main/resources/images/register.png" witdth="827%" height="519">

Fill the information with any info you want to provide (information will be saved locally to you own computer). Limits for fields 4-30 characters (min. 4, max. 30).
In a case username is taken, you will get error message to the username input field.

After pressing register page you will be redirected to login page where you can enter your username/password combination you just created.

**Succesfull login leads to a page below:**
<img src="https://github.com/eherra/catebook/blob/main/src/main/resources/images/frontpage.png" witdth="827%" height="519">

### Searching for other cates
If you want to search for other cates from the platform, press ***Search profiles*** from navigation bar and you will be direceted to **page below:**
<img src="https://github.com/eherra/catebook/blob/main/src/main/resources/images/searchpage.png" witdth="827%" height="519">

From the page you can search for cates around the platform by their cate name.

### Profile page
Hitting from navigation bar ***Profile page*** link, you will be directed to:
<img src="https://github.com/eherra/catebook/blob/main/src/main/resources/images/profilepage.png" witdth="827%" height="519">

In the photo above, you are seeing a profile page as ***a visitor***. 

In a case where you are visiting your ***own*** profile page, the field *(photo below)* appears on the below of the your profile photo where you can add photos to your photo album.

<img src="https://github.com/eherra/catebook/blob/main/src/main/resources/images/loggedUser.png">

At your own profile page and others - you can leave comments to the wall. The comment will include your cate name, time when creating the comment, context and amount of the likes of the comment. (zero likes if hasn't acquired any)

### Photo album
**View from photo album.**
<img src="https://github.com/eherra/catebook/blob/main/src/main/resources/images/albumpage.png" witdth="827%" height="519">
If you are a visitor of the album, you have no access for the buttons "Change to profile photo" and "Delete photo" buttons.

In album you can have max 10 photos at the time. In case you want to add more, please delete photos before adding more.
You can scroll the photos with buttons "previous" and "next" which will appear on top the photo. In a case you are on index 0 at the album, "previous" button disappears and index 9 or last photo of your album - "next" button disappears.

### Friends
On platform you send friend requests to other cates, and if they accept the request - you will become friends. 

You can accept and see your friendlist from your profile page from button ***See friends***.

### Error cases

If the page crashes, change url ending to **/frontpage** and press enter. If still not working, restart the program. Also creating new account from "/register" page should solve the case.

If you forgot your password - create new user. :)



