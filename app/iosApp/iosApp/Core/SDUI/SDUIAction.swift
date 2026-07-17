import Foundation

struct SDUIAction: Decodable {
    let event: String
    
    enum CodingKeys: String, CodingKey {
        case event, type
    }
    
    init(from decoder: Decoder) throws {
        // Primero intentamos como String simple
        if let container = try? decoder.singleValueContainer(),
           let eventString = try? container.decode(String.self) {
            self.event = eventString
        } else {
            // Si falla, intentamos como objeto con la llave 'event'
            let container = try decoder.container(keyedBy: CodingKeys.self)
            self.event = try container.decode(String.self, forKey: .event)
        }
    }
}
