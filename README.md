# energy-map

### Run the server
To be able to access the API you need to create a text file named "token.txt" that contains the API token, place this file in the root of the repo.
To run the server in developer mode execute the following sequence of command:

For Linux:
```bash
cd server
npm install 
npm run dev
```
For Windows:
```bat
cd server
npm install
npm run dev-win
```

### Docker
The project is also available as a docker image. It can then be run on any computer.
To build and run it, type:
```bat
docker build -t energymap .
docker run -p 3000:3000 -e TOKEN=<insert-token> energymap
```
Dont forget to insert the API key without <>.
---

### Dependencies
- Java JDK v17
- NodeJS LTS v18.18 or earlier
- Python
- Visual Studio 2019 + "Desktop development with c++" (Windows only)
