import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { map, Observable } from "rxjs";
import { Product } from "./product.model";
import { Order } from "./order.model";
import { HttpHeaders } from '@angular/common/http';

const PROTOCOL = "http";
const PORT = 8081;
//const PORT = 3500;
@Injectable()
export class RestDataSource {
    baseUrl: string;
    auth_token?: string;

    constructor(private http: HttpClient) {
        this.baseUrl = `${PROTOCOL}://${location.hostname}:${PORT}/store/`;
        // this.baseUrl = `${PROTOCOL}://${location.hostname}:8081/store/`;
        // this.baseUrl = `${PROTOCOL}://${location.hostname}:${PORT}/`;
    }
    getProducts(): Observable<Product[]> {
        console.log('initialize data');
        return this.http.get<Product[]>(this.baseUrl + "products"); //, this.getGetOptions());
    }
    saveOrder(order: Order): Observable<Order> {
        return this.http.post<Order>(this.baseUrl + "orders", order);
    }

    authenticate(user: string, pass: string): Observable<boolean> {
        return this.http.post<any>(this.baseUrl + "login", {
            name: user, password: pass
        }).pipe(map(response => {
            this.auth_token = response.success ? response.token : null;
            return response.success;
        }));
    }

    saveProduct(product: Product): Observable<Product> {
        console.log('Data being sent:', product);
        return this.http.post<Product>(this.baseUrl + "products", product);
            // , this.getOptions());
    }

    updateProduct(product: Product): Observable<Product> {
        return this.http.put<Product>(`${this.baseUrl}product`,
            product, this.getOptions());
    }
    deleteProduct(id: number): Observable<Product> {
        return this.http.delete<Product>(`${this.baseUrl}products/${id}`,
            this.getOptions());
    }
    getOrders(): Observable<Order[]> {
        return this.http.get<Order[]>(this.baseUrl + "orders", this.getOptions());
    }
    deleteOrder(id: number): Observable<Order> {
        return this.http.delete<Order>(`${this.baseUrl}orders/${id}`,
            this.getOptions());
    }
    updateOrder(order: Order): Observable<Order> {
        return this.http.put<Order>(`${this.baseUrl}orders/${order.id}`,
            order, this.getOptions());
    }

    // private getOptions() {
    //     return {
    //         headers: new HttpHeaders({
    //             "Authorization": `Bearer<${this.auth_token}>`
    //         })
    //     }
    // }

    private getOptions() {
        return {
            headers: new HttpHeaders({
                "Authorization": `Bearer<"eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJLN2RMUWdQTG5WYVVPTnBOLVJycGV6cm9DaGU4S3F5VTRuS2xONFBfNlBBIn0.eyJleHAiOjE3MDIwMDE1NjMsImlhdCI6MTcwMTk5NTU2MywianRpIjoiMzBlODIyMWUtZWExMy00NTIzLThmYWQtOGZhMjk2ODA4ZjVlIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MTgwL3JlYWxtcy9tYXN0ZXIiLCJhdWQiOiJzdG9yZWlkIiwic3ViIjoiMzg1Zjk2M2YtNWI5NS00MjJiLTgyNzMtZDE2NjMxYTMwY2QwIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoic3RvcmVpZCIsInNlc3Npb25fc3RhdGUiOiJkMDQyMGQyYi1lYzEyLTRiZjktYWJmYS02MmRhMGI3NDUzOWEiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbIi8qIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJmaXRuZXNzdXNlciIsInN0b3JldXNlciJdfSwic2NvcGUiOiJzdG9yZXNjb3BlIGVtYWlsIHByb2ZpbGUiLCJzaWQiOiJkMDQyMGQyYi1lYzEyLTRiZjktYWJmYS02MmRhMGI3NDUzOWEiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiYXV0aG9yaWVzX2NsYWltbmFtZSI6WyJmaXRuZXNzdXNlciIsInN0b3JldXNlciJdLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJiaWxsIiwiZ2l2ZW5fbmFtZSI6IiIsImZhbWlseV9uYW1lIjoiIn0.ESJLu7gTBw-DkliPhR-qvk78a2SkzvhHPiWzo9EBgLoL4D8n1Zyh_VThvGGU3OXYsKC9iruDpODUyD_hEmD01fL7i56OC0tvV880a3LhuS21KZv91zYS80Os9qJGbOOLGe0ir3qQTcxrAEaYZIqgWYSeKU24SnJeU4OxXSpBTW2TK01wk2Q9q7nqPG8I8-N0NL_JRrHwdB808TmKQ6OXkFlZSAwt5YzTtAgjwvzuE3mJtGfl-6bgeCc_N_VupnlxTIH8v-mteKXmkIGRTqOAukqtwoj09Qhxhd-hCaVZTABR41X6X37CgbnzLbAs586M84y0LBkH5DGbdNbRTt3mNA">`,
                "Access-Control-Allow-Origin": `*`
            })
        }
    }


}