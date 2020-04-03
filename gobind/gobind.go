package gobind

import (
	"github.com/Yukaru-san/DataManager_Client/models"
	"github.com/Yukaru-san/DataManager_Client/server"
)

// Login loggs in
func Login(url, username, password string) [2]string {
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
		return [2]string{}
	}

	if resp.Status == server.ResponseError && resp.HTTPCode == 403 {
		return [2]string{}
	} else if resp.Status == server.ResponseSuccess && len(response.Token) > 0 {
		return [2]string{response.Namespace, response.Token}
	}

	return [2]string{}
}
