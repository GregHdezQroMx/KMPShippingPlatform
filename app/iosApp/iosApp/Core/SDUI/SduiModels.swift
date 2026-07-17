import Foundation

struct SDUIScreenResponse: Decodable {
    let screen: SDUIScreen
}

struct SDUIScreen: Decodable {
    let id: String
    let title: String
    let components: [SDUIComponent]
}

enum SDUIComponent: Decodable, Identifiable {
    case image(SDUIImage)
    case text(SDUIText)
    case textInput(SDUITextInput)
    case selection(SDUISelection)
    case button(SDUIButton)
    case unknown(String)
    
    var id: String {
        switch self {
        case .image(let m): return m.id
        case .text(let m): return m.id
        case .textInput(let m): return m.id
        case .selection(let m): return m.id
        case .button(let m): return m.id
        case .unknown(let id): return id
        }
    }
    
    enum CodingKeys: String, CodingKey {
        case type, id
    }
    
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        let type = try container.decode(String.self, forKey: .type)
        let id = (try? container.decode(String.self, forKey: .id)) ?? UUID().uuidString
        
        switch type {
        case "image": self = .image(try SDUIImage(from: decoder))
        case "text": self = .text(try SDUIText(from: decoder))
        case "text_input": self = .textInput(try SDUITextInput(from: decoder))
        case "select", "selection": self = .selection(try SDUISelection(from: decoder))
        case "button": self = .button(try SDUIButton(from: decoder))
        default: self = .unknown(id)
        }
    }
}

struct SDUIImage: Decodable { let id: String; let label: String?; let imageUrl: String }
struct SDUIText: Decodable { let id: String; let label: String; let style: String? }
struct SDUITextInput: Decodable { let id: String; let label: String; let inputType: String? }

struct SDUISelection: Decodable { 
    let id: String
    let label: String
    let options: [SDUIOption] 
}

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

struct SDUIButton: Decodable { 
    let id: String
    let label: String
    let action: SDUIAction
}

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
