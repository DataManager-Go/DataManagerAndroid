package gobind

import (
	"fmt"

	"github.com/Yukaru-san/DataManager_Client/models"
	"github.com/Yukaru-san/DataManager_Client/server"
)

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
