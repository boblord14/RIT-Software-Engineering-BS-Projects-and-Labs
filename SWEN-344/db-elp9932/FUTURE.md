# Potential Future Plans
This document will detail two potential future systems for the chat system: reactions and threaded conversations.

## Reactions
The reaction system really wouldn't be too difficult to add- such a system really just consists of adding emoji reactions to messages(ideally being able to know who reacted with what too)

#### Tables to add/change
All reactions would need is a 'reaction_list' table. This table would have a few columns in it:

- reactionID primary key: just keeps track of each entry in the table
- messageID foreign key: keeps track of which message the entry is assigned to
- emojiID string: ascii id for the reaction to add
- userID foreign key: keeps track of which user added the reaction

One entry per reaction, even if the emoji is the same. An actual ui would just sum up the unique reactions which postgres can do easily with the SELECT DISTINCT keyword in a query. 

#### API methods to provide

- Add reaction: takes in a message id, username, and an emoji ascii code, creates a new entry in the table for a new reaction.
- Remove reaction: takes in the same args as the add function, removes the entry. A variant that uses reaction id would be prudent to add as well.

Optionally both of the above functions could take in a timestamp argument, check if the user in question is suspended, and reject adding/removing the reaction if they are. Currently suspended users would be able to react(by design).

#### Existing API method changes
Pretty much nothing. The rest of the system can be extended without complication. 

## Threaded Conversations
A little more difficult to add, but much easier to accomplish by updating existing schema rather than adding newer ones. 

#### Tables to add/change
No new tables, just changing existing ones. Add a foreign key field for message_list to message_list, which can serve as a reply marker. If this field has a message id, the message in question is a reply to the message with the id in that field. This can be chained for messages to sort of serve as a "thread"

#### API methods to provide
- A getMessageThread(messageID) method which returns in order a list of every message in a reply chain, both above and below in the chain for the given messageID
- getMessageThreadAbove(messageID) method which functions similarly to getMessageThread but only for messages above the given messageID in the chain
- getMessageThreadBelow(messageID) method which functions similarly to getMessageThread but only for messages below the given messageID in the chain

#### Existing API method changes
Any methods that deal with message_list need to make sure they properly accomidate that it has an extra field. 

Sendmessage(both the channel and the dm variant) need to be updated with an additional argument for a messageID, if there is an id in that slot, the message to send is a reply to the message with the ID in question. Otherwise it just defaults to NULL if that slot in the argument is None or empty. 

Beyond that, shouldn't be any signifigant changes if any. At most just updating indexes in certain test methods. 

