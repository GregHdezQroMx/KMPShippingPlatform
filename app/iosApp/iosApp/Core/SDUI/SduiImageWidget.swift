import SwiftUI

struct SduiImageWidget: View {
    let imageUrl: String
    
    var body: some View {
        AsyncImage(url: URL(string: imageUrl)) { phase in
            switch phase {
            case .success(let image):
                image.resizable()
                    .aspectRatio(contentMode: .fill)
                    .frame(maxWidth: .infinity)
                    .frame(height: 150)
                    .cornerRadius(12)
                    .clipped()
            case .failure(_):
                VStack {
                    Image(systemName: "shippingbox")
                        .font(.system(size: 48))
                        .foregroundColor(.gray.opacity(0.4))
                    Text("Shipping Platform")
                        .font(.caption)
                        .bold()
                        .foregroundColor(.gray)
                }
                .frame(maxWidth: .infinity)
                .frame(height: 150)
                .background(Color.gray.opacity(0.1))
                .cornerRadius(12)
            case .empty:
                ProgressView()
                    .frame(maxWidth: .infinity)
                    .frame(height: 150)
                    .background(Color.gray.opacity(0.1))
                    .cornerRadius(12)
            @unknown default:
                EmptyView()
            }
        }
        .padding(.vertical, 8)
    }
}
