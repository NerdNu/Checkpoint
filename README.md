# Checkpoint

## Overview
A **course** is an organizational structure that holds a list of **checkpoints** (locations that can be set as visited per player) and **triggers**. A trigger is a condition under which an action is executed; whenever the condition is met (current valid triggers are when a player interacts with a particular item or block), its associated action (e.g. setting a checkpoint as visited for the player) is executed.
TODO: needs more thorough explanation

## Commands
All Checkpoint commands require the `checkpoint.admin` permission node.

### `/course`
- `/course <course>`
Selects the course with the given name. After selecting a course, subsequent checkpoint and trigger commands will apply to the selected course.
- `/course add <course>`
Creates a new course with the given name.
- `/course remove <name>`
Removes the course with the given name.
- `/course list`
Shows a list of all available courses.

### `/checkpoint`
- `/checkpoint add <label>`
Creates a checkpoint at your current location with the given label for your currently selected course.
- `/checkpoint remove <label>`
Removes the checkpoint with the given label from your currently selected course.
- `/checkpoint modify order <label> <index>`
Reorders the checkpoint with the given label to the new index. Afterward, `/checkpoint list` will display the reordered checkpoint with its new index.
- `/checkpoint modify location <label>`
- `/checkpoint modify icon <label>`
- `/checkpoint visit <label> [(true|false)]`
- `/checkpoint tp <label>`
- `/checkpoint info <label>`
- `/checkpoint list [page]`
- `/checkpoint reload`

### `/trigger`
TODO: most commands have help text describing their functionality

## Triggers
TODO
## Actions
TODO

## To do
- Command for listing triggers at a specific location / within a radius
- `/trigger reorder` command (changes priority of triggers and ordering in `/trigger list`)
- For checkpoint action, add an option to check whether or not the checkpoint has already been set before teleporting
- Course-specific permissions
