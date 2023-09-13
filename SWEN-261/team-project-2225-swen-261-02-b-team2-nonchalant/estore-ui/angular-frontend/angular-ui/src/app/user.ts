import { Product } from "./product";


export interface User {
    id: number;
    name: string;
    cart: Array<Product>;
  }
