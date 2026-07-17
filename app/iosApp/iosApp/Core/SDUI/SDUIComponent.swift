import Foundation

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

struct SDUIButton: Decodable { 
    let id: String
    let label: String
    let action: SDUIAction
}
