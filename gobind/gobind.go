package gobind

import (
	"fmt"
	"log"
	"net/http"

	"github.com/Yukaru-san/DataManager_Client/models"
	"github.com/Yukaru-san/DataManager_Client/server"
)

// CanConnect return true if go can connect to internet
func CanConnect(u string) string {
	_, err := http.Get(u)
	if err != nil {
		return err.Error()
	}
	return ""
}

// Login loggs in
func Login(url, username, password string) string {
	//Do request
	conf := models.Config{}
	conf.Server.URL = url
	conf.Server.IgnoreCert = false

	var response server.LoginResponse

	resp, err := server.NewRequest(server.EPLogin, server.CredentialsRequest{
		Password: password,
		Username: username,
	}, &conf).Do(&response)

	if err != nil {
		fmt.Println(err)
		return ""
	}

	if resp.Status == server.ResponseError && resp.HTTPCode == 403 {
		fmt.Println(err)
		return ""
	} else if resp.Status == server.ResponseSuccess && len(response.Token) > 0 {
		return response.Token
	}

	return "err"
}

// ListNamespaces list namespaces
func ListNamespaces(url string, token string) server.StringSliceResponse {
	config := models.Config{}
	config.Server.URL = url

	var resp server.StringSliceResponse

	response, err := server.NewRequest(server.EPNamespaceList, nil, &config).WithAuth(server.Authorization{
		Type:    server.Bearer,
		Palyoad: token,
	}).Do(&resp)

	if err != nil {
		if response != nil {
			fmt.Println("http:", response.HTTPCode)
			return server.StringSliceResponse{}
		}
		log.Fatalln(err)
	}

	if response.Status == server.ResponseError {
		fmt.Println(response.Message)
		return server.StringSliceResponse{}
	}

	return resp
}
