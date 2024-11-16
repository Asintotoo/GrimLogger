# GrimLogger
Store GrimAC's logs into a Database with GrimLogger

## What is it?
GrimLogger is a simple plugin which uses [GrimAPI](https://github.com/GrimAnticheat/GrimAPI) to store [GrimAC](https://github.com/GrimAnticheat/Grim) logs into a Database, allowing server administrators to see all the anticheat flag fired by a player with the use of simple command.


## Commands & Permissions
- **/grimlogger reload** *(grimlogger.command.reload)*: Reload the plugin
- **/grimlogger help** *(grimlogger.command.help)*: Show help page
- **/grimlog <player> [page]** *(grimlogger.command.log)*: Show player's log

## Supported Database
- [X] SQL
- [X] SQLite
- [X] MongoDB
- [ ] MariaDB (Coming Soon)


## Requirements
- A Database (if you are not using SQLite)
- [GrimAC](https://github.com/GrimAnticheat/Grim) (of corse)
