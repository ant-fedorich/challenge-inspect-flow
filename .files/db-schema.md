# DB Schema
> `client-backend-json-sample.json`

---

items
- id
- type
- parent_id?
- title?
- content?
- src?
- sort_order

response_sets
- id
- item_id
- multiple_selection: Boolean

responses
- id
- response_set_id
- label
- score?

