/**
 * 
 */


export const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
export const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');