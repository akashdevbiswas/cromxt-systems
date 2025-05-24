export interface PageResponse<T>{
  content: T[],
  last: boolean,
  totalElements: number,
  totalPages: number,
  size: number
}