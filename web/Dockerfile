FROM node:alpine3.18 as node

WORKDIR /usr/src/app

COPY . .

RUN npm version

RUN npm config set legacy-peer-deps true

RUN npm install

RUN npm install -g @angular/cli@16.1.4

RUN npm run build

FROM nginx:1.17.6

COPY ./default.conf /etc/nginx/conf.d/default.conf

COPY --from=node /usr/src/app/dist/evibbra /usr/share/nginx/html

RUN sed -i 's/listen  .*/listen 4200;/g' /etc/nginx/conf.d/default.conf

EXPOSE 4200

CMD ["/bin/sh", "-c", "exec nginx -g 'daemon off;'"]
