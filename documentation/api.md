API overview
============

 # API Documentation

 API provides programmatic access to ETCD data store.

 ## Endpoints

 #### CONFIGS

 - **[<code>GET</code> /configs/](#fetch-all-configs-list)**
 - **[<code>POST</code> /configs/](#create-new-configuration)**
 - **[<code>GET</code> /configs/:name](#get-the-saved-keys-of-any-configuration)**
 - **[<code>PUT</code> /configs/:name](#add-or-update-keys-and-value-in-config)**
 - **[<code>PATCH</code> /configs/:name](#add-or-update-keys-and-value-in-config)**
 - **[<code>DELETE</code> /configs/:name](#delete-config)**
 - **[<code>DELETE</code> /configs/:name/:key](#delete-particular-key-from-config)**

 #### SEARCH

 - **[<code>GET</code> /search?name={name}&data.key={key}](#search-for-key-in-a-config)**
 - **[<code>GET</code> /search?data.key={key}](#search-for-a-key-in-all-config)**

___

# Methods

### CONFIGS

### Fetch all configs list

    GET /configs

Returns all configs from data store.

##### Examples
##### *Sample Request*
    GET /configs
##### *Sample Response*
    [
        "mysql.web",
        "nginx",
        "php"
    ]


### Create new configuration

    POST /configs

##### Body Params:

| Field          |   Format    |Description                                                | Required   |
| -------------- |------------------|----------------------------------------------------- | ---------- |
| `name`         | String           |  The name of the config to create                    | YES        |
| `data`         | String<key,val>  |  Key/Val map to store                                | OPTIONAL   |
| `key`          | String           |  Key to store                                        | OPTIONAL   |
| `val`          | String           |  Val to store                                        | OPTIONAL   |
Creates a new config with the given params.

##### Examples
##### *Sample Request*
    POST /configs
    {
    	"mysql.web":{
    		"bind-address":"127.0.0.1",
    		"max_connections":"100",
    		"connect_timeout":"5"
    	},
    	"nginx":{

    	}
    }

##### *Sample Response*
    {
        "status": "200 OK",
        "message": "SUCCESS",
        "data": "CONFIG CREATED"
    }

### Get the saved keys of any configuration

    GET /configs/:name

##### URL Params:

| Field          | Description                                                       | Required   |
| -------------- | ----------------------------------------------------------------- | ---------- |
| `name`         | The name of the config to get                                     | YES        |


Returns all Key and Value of the config.
##### Examples
##### *Sample Request*
    GET /configs/mysql.web
##### *Sample Response*
    {
        "bind-address": "127.0.0.1",
        "max_connections": "100",
        "connect_timeout": "5"
    }


### Add or Update keys and value in config

    PUT /configs/:name

    PATCH /configs/:name

##### URL Params:

| Field          | Description                                                       | Required   |
| -------------- | ----------------------------------------------------------------- | ---------- |
| `name`         | The name of the config to update                                  | yes        |

##### Body Params

| Field          |   Format    |Description                                                | Required   |
| -------------- |------------------|----------------------------------------------------- | ---------- |
| `data`         | String<key,val>  |  Key/Val map to store                                | YES        |
| `key`          | String           |  Key to store                                        | OPTIONAL   |
| `val`          | String           |  Val to store                                        | OPTIONAL   |

Updates or Add keys to existing config.
##### Examples
##### *Sample Request*
    PUT /configs/mysql.web
    {
            "bind-address": "0.0.0.0",
            "max_connections": "200",
            "connect_timeout": "50"
    }
##### *Sample Response*
    {
        "status": "200 OK",
        "message": "SUCCESS",
        "data": "KEYS UPDATED"
    }

### Delete config

    DELETE /configs/:name

##### URL Params:

| Field          | Description                                                       | Required   |
| -------------- | ----------------------------------------------------------------- | ---------- |
| `name`         | The name of the config to delete                                  | YES        |

Delete the config.
##### Examples
##### *Sample Request*
    DELETE /configs/mysql.web
##### *Sample Response*
    {
        "status": "200 OK",
        "message": "SUCCESS",
        "data": "CONFIG DELETED"
    }

### Delete particular key from config

    DELETE /configs/:name/:key

##### URL Params:

| Field          | Description                                                       | Required   |
| -------------- | ----------------------------------------------------------------- | ---------- |
| `name`         | The name of the config                                            | YES        |
| `key`          | Key to delete from the config                                     | YES        |

Delete key from the config.
##### Examples
##### *Sample Request*
    DELETE /configs/mysql.web/connect_timeout
##### *Sample Response*
    {
        "status": "200 OK",
        "message": "SUCCESS",
        "data": "KEY DELETED"
    }


### SEARCH

### Search for key in a config

    GET /search/?name={name}&data.key={key}

##### Query parameters:

| Field          | Description                                                       | Required   |
| -------------- | ----------------------------------------------------------------- | ---------- |
| `name`         | The name of the config                                            | YES        |
| `key `         | Key to search                                                     | YES        |

Search for the key in the config. Returns matched key.
##### Examples
##### *Sample Request*
    GET /search/?name=mysql.web&data.key=bind-address
##### *Sample Response*
    {
        "mysql.web": {
            "key": "bind-address",
            "value": "0.0.0.0"
        }
    }

### Search for a key in all config

    GET /search/?data.key={key}

##### Query parameters:

| Field          | Description                                                       | Required   |
| -------------- | ----------------------------------------------------------------- | ---------- |
| `key`          | Key to search                                                     | YES        |

Returns all configs with the given key.
##### Examples
##### *Sample Request*
    GET /search?data.key=bind-address
##### *Sample Response*
    {
        "mysql.db": {
            "key": "bind-address",
            "value": "127.0.0.1"
        },
        "mysql.web": {
            "key": "bind-address",
            "value": "0.0.0.0"
        }
    }
