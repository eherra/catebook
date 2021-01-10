## Catebook

A social media platform for cats.

### About Catebook

The project has been made with a help of Java Spring Boot, Thymeleaf template engine for HTML modifications and some Bootstrap and JavaScript here and there. 

Main focus has been on learning backend development, but the front end was not left without attention. (The UX not the best)

### How to get started.

1. Clone the repository 
2. Start the program
#### Terminal command to start the program from root folder (catebook):
```console
mvn compile exec:java -Dexec.mainClass=catebook.Main
```
3. From your browser navigate to **localhost:/8080/register**. (assuming the program started on port 8080)

**A page like this appears:**
<img src="https://github.com/eherra/catebook/blob/main/photos/registernew.png" witdth="827" height="499">

Fill the inputs with info you want to add to your/to your cat's account. Limits for fields:
* Username: 4 to 30 characters
* Password: 6 to infinite* characters
* Catebook name: 4 to 20 characters

<sub><b>*not really</b></sub>

In the case of username is taken, you will get error message to the username input field.

After pressing **Register!** button you will be redirected to login page where you can enter your username/password combination you just created.

**Succesful login leads to a page below:**
<img src="https://github.com/eherra/catebook/blob/main/photos/frontpagee.png" witdth="827" height="499">

### Searching for other cates
If you want to search for other cates from the platform, press ***Search profiles*** from navigation bar and you will be directed to **page below:**
<img src="https://github.com/eherra/catebook/blob/main/photos/searchprofiles.png" witdth="827" height="499">

In order to search all profiles from the platform, press **Go!** without any input string. Otherwise you can search for specific cates around the platform by their cate name.

### Profile page
Hitting from navigation bar ***Profile page*** link, you will be directed to:
<img src="https://github.com/eherra/catebook/blob/main/photos/profilee.png" witdth="827" height="499">

When you are visiting someone else's profile page, the field where you can add photos to the album disappears from the below of the profile photo.

At your own profile page and at other users profile pages - you can leave comments to the wall. The publish will include cate name, time when creating the comment and amount of likes of the comment. (zero if hasn't acquired any)

### Photo album
**A view from photo album.**
<img src="https://github.com/eherra/catebook/blob/main/photos/albumpage.png" witdth="827" height="499">

If you are a visitor of the album, you have no access for buttons **Change to profile photo** and **Delete photo**.

You can scroll the photos with buttons **previous** and **next** which will appear on top the photo. In the case of you are at index 0 at the album, **previous** button disappears and at index of last photo of your album - **next** button disappears.

### Friends
On platform you can send friend requests to other cates, and if they accept the request - you will become friends. 

You can accept friend requests and see your friendlist from your profile page from button ***See friends***.

**A view from friendpage.**
<img src="https://github.com/eherra/catebook/blob/main/photos/friendpage.png" witdth="827" height="499">

### Error cases

Site should work properly on normal use and the links on the site lead to valid places. However, if incorrect url is provided by user - page will inform the user about this by showing a error page. From the error page, you can press any links from the navigation bar to move forward.

If you forgot your password - create new user. :)

### Heroku

Application is hosted on Heroku as well and by default users can't upload any photos on Heroku's free version. (can be uploaded, but some actions when using the database regarding the photo information (fetching and showing the photo/removing photo) will lead to an error.)

However, there's a trick for this and that's why there're a bit odd looking configurations on my code when fetching photos from the database in order to make it work on free version. (photo bytes encoded to a string with a help of [Base64](https://en.wikipedia.org/wiki/Base64) libary when fetching and showing the photo)

Heroku's PostgreSQL database add-on can't be used with this trick so the Heroku application works only while it's running on Heroku's server after starting it (30 minutes after last interaction by any user). After 30 minutes, the database is cleared by Heroku.

