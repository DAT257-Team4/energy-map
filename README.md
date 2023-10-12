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
- Python v2.7
- Visual Studio 2019 + "Desktop development with c++" (Windows only)

It is possible to configure npm to use a custom filepath for python with the below command, this is useful if you need python3 for something else and dont want to add python2 to your path. 
```
npm config set python <path_to_python.exe>
```
This feature was however deprecated in npm v9, so npm v8 is required to do this. Downgrade to npm v8 ussing the following command:
```
npm install -g npm@8
```
