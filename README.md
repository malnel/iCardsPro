# iCardsPro

TO CHECK OUT THIS APP:
1) DOWNLOAD THE CODE AND BUILD A MAVEN APP goals: javafx:jar
2) START UP MySQL SERVER, EG. WITH XAMPP, CREATE AN EMPTY DATABASE
3) CHANGE DB NAME, USERNAME AND PASSWORD IN hibernate.cfg.xml
   KEEP ?characterEncoding=UTF-8 AFTER THE NAME OF DB FOR PROPER POLISH CHARACTER ENCODING
4) RUN THE APP. SIGN UP AS A NEW USER
5) ADD A NEW DECK OF CARDS. YOU CAN UPLOAD SAMPLE FILES FROM SampleFlashcards FOLDER
6) START STUDYING!

Application for studying flashcards.

You can sign up or log in as an existing user.
Your decks of flashcards will be displayed in a table.
You can then choose to study, view statistics, delete deck, add a new deck
or export deck to a txt file.

You can add new decks by typing front and back of the card or by uploading a text file
of this format :

- 	#comment
-	front | back
-	front | back
	
When you add a card, it is initially assigned a skill of 3. When you study and answer
correctly - the skill will go up by 1 point. If your answer is wrong, skill will drop by 1.
The range of skill is 1-5.

As you start a study session you get to choose which cards you want to view:
- all
- skill 4 or lower,
- skill 3 or lower,
etc ...
You can also add all cards you have marked as starred
or all cards you answered incorrectly last time you studied.

I created this app with the use of:
- JavaFX for the beautiful looks,
- Hibernate for the database connection,
- Maven as my build automation tool.

Enjoy!