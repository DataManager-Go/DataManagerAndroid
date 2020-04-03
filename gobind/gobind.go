package gobind

import (
	"github.com/Yukaru-san/DataManager_Client/models"
	"github.com/Yukaru-san/DataManager_Client/server"
)

// Greet greets
func Greet() string {
	return "greetings"
}

// Login loggs in
func Login(url, username, password string) []string {
	//Do request
	conf := models.Config{}
	conf.Server.URL = url

	var response server.LoginResponse

	resp, err := server.NewRequest(server.EPLogin, server.CredentialsRequest{
		Password: password,
		Username: username,
	}, &conf).Do(&response)

	if err != nil {
		return []string{}
	}

	if resp.Status == server.ResponseError && resp.HTTPCode == 403 {
		return []string{}
	} else if resp.Status == server.ResponseSuccess && len(response.Token) > 0 {
		return []string{response.Namespace, response.Token}
	}

	return []string{}
}
