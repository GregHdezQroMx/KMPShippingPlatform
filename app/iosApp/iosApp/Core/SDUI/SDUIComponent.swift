import Foundation

enum SDUIComponent: Decodable, Identifiable {
    case image(SDUIImage)
    case text(SDUIText)
    case textInput(SDUITextInput)
    case selection(SDUISelection)
    case button(SDUIButton)
    case card(SDUICard)
    case icon(SDUIIcon)
    case unknown(String)
    
    var id: String {
        switch self {
        case .image(let m): return m.id
        case .text(let m): return m.id
        case .textInput(let m): return m.id
        case .selection(let m): return m.id
        case .button(let m): return m.id
        case .card(let m): return m.id
        case .icon(let m): return m.id
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
        case "card": self = .card(try SDUICard(from: decoder))
        case "icon": self = .icon(try SDUIIcon(from: decoder))
        default: self = .unknown(id)
        }
    }
}
