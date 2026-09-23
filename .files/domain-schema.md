# Domain Schema
> `client-backend-json-sample.json`

## Domain entities

Item
- id: Int
- type: ItemType

ItemType: page | section | text | image |choice

PageItem: Item
- id: Int
- type: ItemType
- title: String
- items: [Item]

SectionItem: Item
- id: Int
- type: ItemType
- title: String
- items: [Item]

TextQuestionItem: Item
- id: Int
- type: ItemType
- content: String

ImageQuestionItem: Item
- id: Int
- type: ItemType
- title: String
- src: String

ChoiceQuestionItem: Item
- id: Int
- type: ItemType
- content: String
- responseSet: ResponseSet

ResponseSet
- id: Int
- multipleSelection: Boolean
- responses: [Response]

Response
- id: Int
- label: String
- score: Int?

