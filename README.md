## Catebook

A social media platform for cats.

### About Catebook

The project has been made with a help of Java Spring boot and Thymeleaf template engine for HTML modifications and some Bootstrap here and there. 

Main focus has been on learning backend development, but the front end was not left without attention. (The UX might not be the best)

### How to get started.

1. Clone the repository 
2. Start the program
#### Terminal command to start the program from root folder (catebook):
```console
mvn compile exec:java -Dexec.mainClass=catebook.Main
```
3. From your browser navigate to **localhost:/8080/register**. (assuming the program started on port 8080)

**Page like this appears:**
<img src="https://github.com/eherra/catebook/blob/main/photos/registernew.png" witdth="827" height="499">

Fill the inputs with info you want to add to your/to your cat's account. Limits for fields 4-30 characters (min. 4, max. 30).
In the case of username is taken, you will get error message to the username input field.

After pressing **Register!** button you will be redirected to login page where you can enter your username/password combination you just created.

**Succesful login leads to a page below:**
<img src="https://github.com/eherra/catebook/blob/main/photos/frontpage.png" witdth="827" height="499">

### Searching for other cates
If you want to search for other cates from the platform, press ***Search profiles*** from navigation bar and you will be direceted to **page below:**
<img src="https://github.com/eherra/catebook/blob/main/photos/searchprofiles.png" witdth="827" height="499">

From the page you can search for cates around the platform by their cate name.

### Profile page
Hitting from navigation bar ***Profile page*** link, you will be directed to:
<img src="https://github.com/eherra/catebook/blob/main/photos/profile.png" witdth="827" height="499">

When you are visiting someone else's profile page, the field where you can add photos to the album disappears from the below of the profile photo.

At your own profile page and at other users profile pages - you can leave comments to the wall. The publish will include cate name, time when creating the comment and amount of the likes of the comment. (zero likes if hasn't acquired any)

### Photo album
**View from photo album.**
<img src="https://github.com/eherra/catebook/blob/main/photos/albumpage.png" witdth="827" height="499">
If you are a visitor of the album, you have no access for buttons **Change to profile photo** and **Delete photo**.

You can scroll the photos with buttons **previous** and **next** which will appear on top the photo. In the case of you are at index 0 at the album, **previous** button disappears and at index of last photo of your album - **next** button disappears.

### Friends
On platform you can send friend requests to other cates, and if they accept the request - you will become friends. 

You can accept friend requests and see your friendlist from your profile page from button ***See friends***.

**View from friendpage.**
<img src="https://github.com/eherra/catebook/blob/main/photos/friendpage.png" witdth="827" height="499">

### Error cases

Site should work properly on normal use and the links on the site lead to valid places. However, if incorrect url is provided by user - page will inform the user about this by showing a error page. If something weird happens, navigate to **/frontpage**.

If you forgot your password - create new user. :)



