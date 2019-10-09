# MealPlanner 

**Acknowledgements:**

1) Seanyles: For developing and testing the Stock classes, Intake classes and overall mentorship throughout the course of the project. 
2) Liam Weldon: For developing and testing the User classes and the Front End.
3) Andrew Ricci: For developing and testing the Product classes.

**Project Overview:**

Note: The project was developed in a team of 5, for an open ended class project during the months of March and April of 2019. 

It is all too often that we buy products or items and leave them in our fridges or our pantries, only to forget about them. However, we tend to find them again later when they’ve been expired and can no longer be used. Motivated by this issue, we wanted to create an application that could help combat this problem. However, we also wanted to create something that was unique and allowed us to expand our knowledge of databases. For these reasons, we came up with the idea of an intelligent inventory tracker, an application which would keep track of the various products that a user has in their pantry, in an attempt to minimize the overall wastage of food, while also giving users a platform to share and search for recipes.

**Project Features:**

**1) Inventory Tracking**

The first feature of this project is its ability to keep track of a user’s stock of ingredients, maintaining how much of what ingredient they have. This would ensure that a user can get a complete overview of what they have in their pantries each time they check the application. 

**2) Creating and Searching for Recipes**

The second feature of this project is that it allows users to create recipes and search for them based on Tags, which contain a String that describes the recipe. A tag can be a cuisine (“Indian”, “Mexican” etc.), a dietary restriction (“Vegan”, “Low-Fat” etc.) or even a Recipe type (“Appetizer”, “Main Course” etc.). Every Recipe has a set of Tags and a user can find a recipe he likes by searching based on the tags.  

**3) The “What Recipes Can I Cook?” Feature**

The third feature allows users to search for every recipe that they can cook based on the ingredients they already have in their pantry. This feature is important to our overall goal of minimizing food wastage because it gives users feasible solutions as to what recipes they can cook without them having to purchase more items. 

**4) Following and Unfollowing of Users**

The fourth feature of this project is the ability for a user to follow and unfollow other users. Our idea behind this to mimic a social media, where the user can be notified each time a user of the application uploads a new recipe. That being said, although we did implement this feature we were unable to implement the notification system given the time constraints we had for the project. 

**5) User Rating** 

The fifth feature of this project is that it allows users to rate recipes. A user can rate a recipe on a scale of 1 to 5 and a recipe will display its Average score based on ratings from a number of different users. In addition, every recipe query sorts the recipes by rating, such that users can see better rated recipes first. 

**6)  Automatic Stock Intakes**

Each time a user consumes a recipe, the application automatically updates the users stock based on the ingredients the recipe required. That being said, at this moment there is no way to rollback or edit an Intake, which means that once a recipe is consumed it is not possible to change the users stock back to its original as this would be very complex.  

Note Regarding Front End: 

The front end is bare bones at the moment. This is largely due to the time constraints in place and that only Liam had any front end experience in the group.  
