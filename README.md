# PicroMyAuth

Simple authentication plugin that uses MySQL to store player nick/pass.
Made this for my private SMP\
Does work on 1.19.2, 1.20.1

## Basic commands
`/changepwd <new_password>` - changes password\
`/login <password>` - I think it's already clear to you

## Service (admin)
`/smpadd <nickname>` - adds player to the database (whitelist) and then returns recovery UUID and password\
`/db <status | reconnect>` - service command\
> argument `status` returns connection status to the database\
> argument `reconnect` reconnects to the database

## Permissions
`myauth.admin` - provides admin commands

## Bugs (checkmark if fixed)
- [ ] After 8 hours plugin disconnects from the db

## Acknowledgements
Огромное спасибо всем гостям и активным участникам за игру на GeekCraft SMP! Может когда-нибудь снова увидимся.\
PS: Следить за моими проектами можете здесь https://www.youtube.com/@eldegende и тут https://picro.neocities.org/ (блог еще не готов)\
Надеюсь, что когда-нибудь закончу Parkour Paradise, поэтому следите за обновлениями ^^^

🪦 GeekCraft SMP | 17.12.2022 - 01.09.2023
