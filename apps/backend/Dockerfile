# Dockerfile - backend
FROM node:22.11.0-alpine

WORKDIR /usr/src/app

# Ative o Corepack e configure a versão do Yarn
RUN corepack enable && corepack prepare yarn@4.5.1 --activate

# Copie arquivos do contexto global do monorepo
COPY package.json yarn.lock ./

# Copie os arquivos específicos do workspace backend
COPY ./apps/backend/package.json ./apps/backend/

# Instale dependências do monorepo
RUN yarn install

# Copie o código do backend
COPY ./apps/backend ./apps/backend/

# Exponha a porta da aplicação
EXPOSE 3000

# Comando para rodar o servidor
CMD [ "yarn", "start:backend" ]
