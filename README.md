# Worlds of Beleriand
A large-scale, text-based RPG loosely based on the fictional worlds of Middle Earth prior to the destruction of Numenor.

The game works using a Spring-Boot powered rest API backend, which clients will connect to using username/password JWT authentication. Clients will then send HTTP requests to perform actions and gain information about the world around them.

The server is meant to be hosted centrally, such that you can always connect to it simply by starting your client.

To build the client, clone this repository, then
```bash
cd client
mvn package
```
and look for the "jar-with-dependencies" jar file.

## Functionality
Right now this list shows the things in the game/API which are functional, and those which are planned.

- [x] Authentication.
- [x] Persistent user state information.
- [x] API support for chat.
- [ ] Client support for chat.
- [x] Movement to different locations via pathways.
- [ ] Items, in the user's inventory, and randomly generating in certain locations over time. Crafting of certain items.
- [ ] Skills, which users (and other entities) can acquire over time by doing certain actions.
- [ ] Combat between users and other users, or users and other game entities.
- [ ] Quests: dialog, quest state information.
- [ ] Trading between players.
