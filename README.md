# Carpooling Workshop

---

## Technology & Resources

- **Android Studio**
- **Java**
- **SQLite**
- **Google Maps Services API**



## Functionalities

- **Register** and **login** as a User
- **Admin** panel
- Different menus for each User type
- Choice between two roles: **Driver** & **Passenger**
- **Register** your own **Vehicle** with all necessary details
- Set **Origin** & **Destination** (or Start and Finish point on map)
- Set active *Date* & *time*
- View available **Offers** within a 5000m radius (potentially adjustable)
- Make **Offer** as Passenger to the corresponding Driver
- Accept the Offer as a Driver
- Mark the Offer (or **Drive**) completed as a Driver, aswell as payment details and **Rating**

## Database Schema

### Models (Tables):

- User Model
- Vehicle Model
- Drive Model
- Rating Model

### Relationships:

- Each **User** can have *multiple* **Vehicles**. Each Vehicle has **FK** to User.
- Each **User** has only one *active* **Vehicle**.
- Each **Drive** has **FKs** to Driver and Passenger (or two **FKs** to User Model).
- Each **Rating** has **FKs** to *rated* User and *rater* User.

## Folder Structure, Activities, Layouts & General Overview of the App

![1](/readme_assets/images/1.png)
![2](/readme_assets/images/2.png)
![3](/readme_assets/images/3.png)
![4](/readme_assets/images/4.png)
![5](/readme_assets/images/5.png)
![6](/readme_assets/images/6.png)
![7](/readme_assets/images/7.png)
![8](/readme_assets/images/8.png)
![9](/readme_assets/images/9.png)
![10](/readme_assets/images/10.png)
![11](/readme_assets/images/11.png)
![12](/readme_assets/images/12.png)
![13](/readme_assets/images/13.png)
![14](/readme_assets/images/14.png)

...