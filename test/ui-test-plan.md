# Nova UI Test Plan

## Greeting and farewell
Aim: Verify that the chatbot starts and exits cleanly.
Inputs:
    bye
Expected output:
    ____________________________________________________________
     _   _    ___    _   _    _
    | \ | |  / _ \  | | | |  / \
    |  \| | | | | | | | | | / _ \
    | |\  | | |_| |  \ V / / ___ \
    |_| \_|  \___/    \_/ /_/   \_\
    Hello! I'm Nova.
    What can I do for you?
    ____________________________________________________________
    ____________________________________________________________
    Bye. Hope to see you again soon!
    ____________________________________________________________

## Handle invalid commands
Aim: Verify that an empty todo and an unrecognized command produce errors without adding tasks.
Inputs:
    todo
    blah
    list
    bye
Expected output:
    ____________________________________________________________
     _   _    ___    _   _    _
    | \ | |  / _ \  | | | |  / \
    |  \| | | | | | | | | | / _ \
    | |\  | | |_| |  \ V / / ___ \
    |_| \_|  \___/    \_/ /_/   \_\
    Hello! I'm Nova.
    What can I do for you?
    ____________________________________________________________
    ____________________________________________________________
     Please add a description after 'todo'.
    ____________________________________________________________
    ____________________________________________________________
     I don't recognize that command.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
    ____________________________________________________________
    ____________________________________________________________
    Bye. Hope to see you again soon!
    ____________________________________________________________

## Mark and unmark tasks
Aim: Verify that task completion status can be set and reversed.
Inputs:
    todo read book
    todo return book
    mark 1
    mark 2
    unmark 2
    list
    bye
Expected output:
    ____________________________________________________________
     _   _    ___    _   _    _
    | \ | |  / _ \  | | | |  / \
    |  \| | | | | | | | | | / _ \
    | |\  | | |_| |  \ V / / ___ \
    |_| \_|  \___/    \_/ /_/   \_\
    Hello! I'm Nova.
    What can I do for you?
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
      [T][ ] read book
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
      [T][ ] return book
     Now you have 2 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Nice! I've marked this task as done:
       [T][X] read book
    ____________________________________________________________
    ____________________________________________________________
     Nice! I've marked this task as done:
       [T][X] return book
    ____________________________________________________________
    ____________________________________________________________
     OK, I've marked this task as not done yet:
       [T][ ] return book
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][X] read book
     2.[T][ ] return book
    ____________________________________________________________
    ____________________________________________________________
    Bye. Hope to see you again soon!
    ____________________________________________________________

## Delete tasks
Aim: Verify that a task can be removed by its one-based position and that invalid task numbers produce an error without changing the list.
Inputs:
    todo read book
    todo return book
    delete 1
    delete 3
    list
    bye
Expected output:
    ____________________________________________________________
     _   _    ___    _   _    _
    | \ | |  / _ \  | | | |  / \
    |  \| | | | | | | | | | / _ \
    | |\  | | |_| |  \ V / / ___ \
    |_| \_|  \___/    \_/ /_/   \_\
    Hello! I'm Nova.
    What can I do for you?
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
      [T][ ] read book
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
      [T][ ] return book
     Now you have 2 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Noted. I've removed this task:
       [T][ ] read book
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Please provide a valid task number.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] return book
    ____________________________________________________________
    ____________________________________________________________
    Bye. Hope to see you again soon!
    ____________________________________________________________

## Handle malformed task and date commands
Aim: Verify that malformed task numbers and deadline or event formats display errors and do not add or change tasks.
Inputs:
    todo read book
    mark 0
    mark 3
    unmark abc
    unmark 0
    delete
    delete -1
    delete abc
    deadline read book
    deadline /by Friday
    event meeting /from Monday
    event meeting /to Tuesday
    list
    bye
Expected output:
    ____________________________________________________________
     _   _    ___    _   _    _
    | \ | |  / _ \  | | | |  / \
    |  \| | | | | | | | | | / _ \
    | |\  | | |_| |  \ V / / ___ \
    |_| \_|  \___/    \_/ /_/   \_\
    Hello! I'm Nova.
    What can I do for you?
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
      [T][ ] read book
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Please provide a valid task number.
    ____________________________________________________________
    ____________________________________________________________
     Please provide a valid task number.
    ____________________________________________________________
    ____________________________________________________________
     Please provide a valid task number.
    ____________________________________________________________
    ____________________________________________________________
     Please provide a valid task number.
    ____________________________________________________________
    ____________________________________________________________
     Please provide a valid task number.
    ____________________________________________________________
    ____________________________________________________________
     Please provide a valid task number.
    ____________________________________________________________
    ____________________________________________________________
     Please provide a valid task number.
    ____________________________________________________________
    ____________________________________________________________
     Please use: deadline DESCRIPTION /by DATE.
    ____________________________________________________________
    ____________________________________________________________
     Please use: deadline DESCRIPTION /by DATE.
    ____________________________________________________________
    ____________________________________________________________
     Please use: event DESCRIPTION /from START /to END.
    ____________________________________________________________
    ____________________________________________________________
     Please use: event DESCRIPTION /from START /to END.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] read book
    ____________________________________________________________
    ____________________________________________________________
    Bye. Hope to see you again soon!
    ____________________________________________________________

## Maintain task positions across task types
Aim: Verify that deleting a middle task keeps the remaining task order and positions correct across todo, deadline, and event tasks.
Inputs:
    todo read book
    deadline submit assignment /by Friday
    event project meeting /from Monday 2pm /to 4pm
    delete 2
    mark 2
    list
    bye
Expected output:
    ____________________________________________________________
     _   _    ___    _   _    _
    | \ | |  / _ \  | | | |  / \
    |  \| | | | | | | | | | / _ \
    | |\  | | |_| |  \ V / / ___ \
    |_| \_|  \___/    \_/ /_/   \_\
    Hello! I'm Nova.
    What can I do for you?
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
      [T][ ] read book
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
      [D][ ] submit assignment (by: Friday)
     Now you have 2 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
      [E][ ] project meeting (from: Monday 2pm to: 4pm)
     Now you have 3 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Noted. I've removed this task:
       [D][ ] submit assignment (by: Friday)
     Now you have 2 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Nice! I've marked this task as done:
       [E][X] project meeting (from: Monday 2pm to: 4pm)
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] read book
     2.[E][X] project meeting (from: Monday 2pm to: 4pm)
    ____________________________________________________________
    ____________________________________________________________
    Bye. Hope to see you again soon!
    ____________________________________________________________

## Add and list todo tasks
Aim: Verify that todo tasks use the T prefix and report the task count when added.
Inputs:
    todo borrow book
    todo buy bread
    list
    bye
Expected output:
    ____________________________________________________________
     _   _    ___    _   _    _
    | \ | |  / _ \  | | | |  / \
    |  \| | | | | | | | | | / _ \
    | |\  | | |_| |  \ V / / ___ \
    |_| \_|  \___/    \_/ /_/   \_\
    Hello! I'm Nova.
    What can I do for you?
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
      [T][ ] borrow book
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
      [T][ ] buy bread
     Now you have 2 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] borrow book
     2.[T][ ] buy bread
    ____________________________________________________________
    ____________________________________________________________
    Bye. Hope to see you again soon!
    ____________________________________________________________

## Add and list deadline tasks
Aim: Verify that deadline tasks preserve their plain-text deadline and display the D prefix.
Inputs:
    deadline return book /by Sunday
    deadline submit assignment /by 11:59 pm
    list
    bye
Expected output:
    ____________________________________________________________
     _   _    ___    _   _    _
    | \ | |  / _ \  | | | |  / \
    |  \| | | | | | | | | | / _ \
    | |\  | | |_| |  \ V / / ___ \
    |_| \_|  \___/    \_/ /_/   \_\
    Hello! I'm Nova.
    What can I do for you?
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
      [D][ ] return book (by: Sunday)
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
      [D][ ] submit assignment (by: 11:59 pm)
     Now you have 2 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[D][ ] return book (by: Sunday)
     2.[D][ ] submit assignment (by: 11:59 pm)
    ____________________________________________________________
    ____________________________________________________________
    Bye. Hope to see you again soon!
    ____________________________________________________________

## Add and list event tasks
Aim: Verify that event tasks preserve their plain-text start and end values and display the E prefix.
Inputs:
    event project meeting /from Mon 2pm /to 4pm
    event workshop /from Friday 9am /to Friday 11am
    list
    bye
Expected output:
    ____________________________________________________________
     _   _    ___    _   _    _
    | \ | |  / _ \  | | | |  / \
    |  \| | | | | | | | | | / _ \
    | |\  | | |_| |  \ V / / ___ \
    |_| \_|  \___/    \_/ /_/   \_\
    Hello! I'm Nova.
    What can I do for you?
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
      [E][ ] project meeting (from: Mon 2pm to: 4pm)
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
      [E][ ] workshop (from: Friday 9am to: Friday 11am)
     Now you have 2 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[E][ ] project meeting (from: Mon 2pm to: 4pm)
     2.[E][ ] workshop (from: Friday 9am to: Friday 11am)
    ____________________________________________________________
    ____________________________________________________________
    Bye. Hope to see you again soon!
    ____________________________________________________________
