package gobind

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"strings"

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
func Login(url, username, password, machineID string) string {
	//Do request
	conf := models.Config{}
	conf.Server.URL = url
	conf.Server.IgnoreCert = false

	var response server.LoginResponse

	resp, err := server.NewRequest(server.EPLogin, server.CredentialsRequest{
		Password:  password,
		Username:  username,
		MachineID: machineID,
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
func ListNamespaces(url string, token string) string {
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
			return ""
		}
		log.Fatalln(err)
	}

	if response.Status == server.ResponseError {
		fmt.Println(response.Message)
		return ""
	}

	s, _ := json.Marshal(resp.Slice)
	return string(s)
}

// ListFiles lists files
func ListFiles(token, url, name, namespace, group, tag string, id int) string {
	tags := []string{}
	if len(tag) > 0 {
		tags = strings.Split(tag, ",")
	}
	groups := []string{}
	if len(group) > 0 {
		groups = strings.Split(group, ",")
	}

	config := models.Config{}
	config.Server.URL = url

	var filesResponse server.FileListResponse
	response, err := server.NewRequest(server.EPFileList, &server.FileListRequest{
		FileID: uint(id),
		Name:   name,
		Attributes: models.FileAttributes{
			Namespace: namespace,
			Groups:    groups,
			Tags:      tags,
		},
		OptionalParams: server.OptionalRequetsParameter{
			Verbose: 3,
		},
	}, &config).WithAuth(server.Authorization{
		Type:    server.Bearer,
		Palyoad: token,
	}).Do(&filesResponse)

	if err != nil || response.Status == server.ResponseError {
		return ""
	}

	s, _ := json.Marshal(filesResponse.Files)
	return string(s)
}
