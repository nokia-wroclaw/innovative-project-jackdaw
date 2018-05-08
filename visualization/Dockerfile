FROM node:carbon

# Create app directory
WORKDIR /usr/src/visualization

# Install app dependencies
# A wildcard is used to ensure both package.json AND package-lock.json are copied
# where available (npm@5+)
COPY package*.json ./

RUN npm install
# If you are building your code for production
# RUN npm install --only=production

# Bundle app source
COPY . .

RUN apt-get update && apt-get install -y netcat-openbsd
ENTRYPOINT ["./docker-entrypoint.sh"]

EXPOSE 3000
CMD [ "npm", "start" ]
