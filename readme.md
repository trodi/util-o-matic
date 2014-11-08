# util-o-matic
Umbrella project to hold little utilities I've written...

## utilities
### scrapeMovieInfo
Takes a file full of movie titles and queries the open movie database for additional information. Prints out easily imported to a spreadsheet as a csv.

## Development
Written in Scala. Dependency management and build handled by gradle.

### TODO
* [x] initial integration with gradle
* [x] gradle fully build out and able to run (initially via bat file generated from application plugin) 
* [ ] have build create a fat jar for simplistic running (one per util). maybe wrap in an exe for fun
