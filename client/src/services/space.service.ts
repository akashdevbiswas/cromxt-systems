import { Injectable } from "@angular/core";
import { AuthService } from "./auth.service";
import { HttpClient } from "@angular/common/http";
import { PageResponse } from "../util/page.response";


@Injectable({providedIn: 'root'})
export class SpaceService{
  constructor(private authService:AuthService, private httpClient:HttpClient) {}


  createSpace(){

  }

  fetchSpaces(){
    if(!this.authService.getAuthrization()) {
      return null;
    }
    return this.httpClient.get<PageResponse<Spaces>>('/server/api/v1/spaces',{
      headers:{
        'Authorization': `Bearer ${this.authService.getAuthrization()}`
      },
      params:{
        page: 0,
        size: 10
      }
    });
  }

}

export interface Spaces{
  id: string;
  spaceName: string;
  spaceDescription: string;
  spaceType: string;
  spaceOwner: string;
}

export interface Space{
  spaceName: string;
  spaceDescription: string;
  spaceType: string;
}