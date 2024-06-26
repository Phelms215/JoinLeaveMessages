## JoinLeave Messages 2.0

Basic mod that by default disables the Join/Leave messages on a server. Configuration shown below allows a lot of flexibility to messages sent.

Supports Fabric & Forge
1.18.2
1.19.3+

#### Configuration File:</strong> Location At config/joinleavemessages.properties
```
# WARNING: Enables debug for all server logs
debug = false

# use %p to replace the players name.
# use %o for the old players name (only available on rename)
# colors work with the same system as Minecraft MOTD

# Send a message when a player joins a server for the first time
first_join_message_enabled              = false

# Defaults to in game join message if set to false
custom_first_join_message               = false
custom_first_join_message_text          = \u00a75\u00a7lWelcome to our server for the first time %p!

# Send a message the first time a player joins after changing their minecraft name
rename_message_enabled                  = false
custom_rename_message                   = false
custom_rename_message_text              = Hello %o or should I say %p!

# Send a message anytime a player joins the server
# Is NOT sent with the first join message
join_message_enabled                    = false
custom_join_message                     = false
custom_join_message_text                = Hello again %p!

# Send a message anytime a player leaves the server
leave_message_enabled                   = false
custom_leave_message                    = false
custom_leave_message_text               = \u00a7e\u00a7l%p signs off!

# Send a private message when the player first joins.
private_first_join_message              = false
private_first_join_message_text         = Glad you are here %p! If you need any help just let us know!

# Send a message only to the user who signed on.
# Is NOT sent with the first private join message
private_join_message                    = false
private_join_message_text               = Welcome back %p! If you need any help just let us know!
```

## Support This Project
Never required, but always appreciated. 

[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/B0B1J6ITH)
