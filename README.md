# Multi-currency transfer platform
The platform allows you to store amounts in different currencies and perform currency transfers. Amounts are converted at the exchange rate available at the time at which the operation is performed, thanks to a free usage of [freecurrencyapi.com](https://freecurrencyapi.com/). User logs via login and password. Passwords are hashed with SHA256 and a salt. The project operates on simplified database, which simulates real system. The database is immune to SQL injection thanks to using java.sql.

![db diagram](https://raw.githubusercontent.com/barankonrad/currencyPlatform/main/images/db.png)

<p align="center">
  <img src="https://raw.githubusercontent.com/barankonrad/currencyPlatform/main/images/api.png"/>
</p>

# Project functionalities

- User login using login and password. Passwords are hashed with SHA256 and a salt.
- User registration using the app. The user is informed when the chosen login is occupied.
- Within the friends’ list, users can perform transfers.
- Transfers are multi-currency, and money is being exchanged based on the latest rates from API.
- Browsing the user’s transaction history
- Displaying the latest currency exchange rates for a chosen time span

<p align="center">
  <img src="https://raw.githubusercontent.com/barankonrad/currencyPlatform/main/images/ratesGif.gif"/>
</p>

# Technologies
Things I used in this project:
- Java
- JavaFX  - GUI
- Maven - project managment
- Gson - parsing API data
- MSSQL - database managment
- JDBC - database connection

# Project goals
The main purpose of the project was to find usage of the acquired knowledge of MSSQL transactions, develop JavaFX skills and get familiar with using API. Moreover, the project uses password hashing with a salt, as I have always been curious about this.
