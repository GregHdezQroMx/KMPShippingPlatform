import Foundation

struct SDUIOption: Decodable, Hashable { 
    let value: String 
    let label: String 
    
    enum CodingKeys: String, CodingKey {
        case value, id, label
    }
    
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.label = try container.decode(String.self, forKey: .label)
        // Soporte para ambos esquemas: 'id' o 'value'
        self.value = (try? container.decode(String.self, forKey: .value)) ?? 
                     (try? container.decode(String.self, forKey: .id)) ?? ""
    }
}
