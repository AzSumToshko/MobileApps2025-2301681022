# Folder Structure
The folder structure was created by going in example github repositories with MVVM architecture and seeing how they have done it. Underneath this is what each folders purpose is and why ive added it.


data/local/dao --> the SQL queries
data/local/database --> opens and creates the DB file
data/local/entity --> the DB table (what a car looks like as a row)

data/mapper --> converts entity <--> model

data/remote/api --> the API interface
data/remote/dto --> the raw JSON objects from the API
data/remote/source --> fetches data from the API

data/repository --> middle man, decides if data comes from DB or network

di --> dependency injection setup

domain/model --> the clean data classes the app actually uses
domain/repository --> interface that defines what the repo can do
domain/usecase --> single-purpose logic (e.g. GetAllCarsUseCase)

ui/navigation --> screen routes and nav graph
ui/screens --> all the screens (home, detail, search, etc.)
ui/theme --> colors, typography, Material3 theme

util --> small helper functions used across the app
