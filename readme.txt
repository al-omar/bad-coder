Design and Assumptions:
The problem can be divided into two parts -
1. Optimally using time provided before lunch so that it is fully utilized
2. If Time before lunch is used in best possible way, then remaining tasks can be scheduled by Largest remaining time first algorithm.

Number of tracks to be used -
One track can have mmaximum of 7 hours (9:00AM to 12:00PM and 1:00PM to 5:00PM). We can calculate total time taken by all tasks and find total n
umber of trackks required to complete all tasks. For Ex - If All tasks take 23 hours, then we will need 4 tracks because 23mod7+1 =4

Problem 1-
Problem 1 can be seen as a list of numbers(minutes) and we want to find group of numbers from this list that sum up to 180 minutes or closest to 180 minutes(9:00AM to 12:00PM). We can sort the time used by tasks in non-increasing order and recursively find a set that can use provided 180 minutes.For Each track, We will solve this problem iteratively to find best possible usage of time before lunch break.

Problem 2-
After Lunch, We will simply allocate task to that particular track, which has most amount of remaining time to reach 5:00PM bell. It is largest remaining time algorithm, where initially each track has remaining time of 4 hours.

Once All tasks are completed, we can declare Networking Event.

Data Transfer Object Classes used -
1. Task - Defining each task that needs to be scheduled. Features - TaskId, Title, duration
2. Tracks - List of task in track, time used by that track, track Id
